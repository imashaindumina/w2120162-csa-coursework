# Overview
This project has been designed based on REST architecture and is part of the 5COSC022W Client-Server Architectures coursework. The web application creates a Smart Campus environment that allows users to create and manage rooms, sensors, and past sensor readings.

JAX-RS technology (using Jersey) is used to implement the REST API, which is hosted on Apache Tomcat. As specified in the coursework, data persistence has been done using in-memory HashMap and ArrayList data structures.

# API Design
The API follows RESTful architectural principles with a logical resource hierarchy.

## Base URL
http://localhost:8080/SmartCampusAPI/api/v1

## Main Resources

| Resource | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| Discovery | GET | /api/v1 | Returns API metadata and resource links. |
| Rooms | GET | /rooms | Retrieve all rooms. |
| | POST | /rooms | Create a new room. |
| | GET | /rooms/{id} | Get specific room details. |
| | DELETE | /rooms/{id} | Remove a room (blocked if sensors exist). |
| Sensors | GET | /sensors | Retrieve all sensors (supports ?type= filter). |
| | POST | /sensors | Register a sensor (requires valid roomId). |
| | DELETE | /sensors/{id} | Remove a sensor. |
| Readings | GET | /sensors/{id}/readings | Get historical readings for a sensor. |
| | POST | /sensors/{id}/readings | Append a new reading (updates lastReading). |

# Key Features
* HATEOAS Discovery: Root endpoint providing navigation links.
* Referential Integrity: Prevents deleting rooms with active hardware (409 Conflict).
* Dependency Validation: Ensures sensors are linked to existing rooms (422 Unprocessable Entity).
* State Constraints: Blocks readings from sensors in MAINTENANCE mode (403 Forbidden).
* Deep Nesting: Uses the Sub-Resource Locator pattern for readings.
* Observability: Global Request/Response logging filters.
* Leak-proof: Global exception handling to hide Java stack traces.

# Technology Stack
* Language: Java 8
* Framework: JAX-RS (Jersey Implementation)
* Server: Apache Tomcat 9
* Build Tool: Maven
* Data: In-memory Collections (Thread-safe logic)

# How to Build and Run
## Prerequisites
* JDK 1.8
* Apache NetBeans
* Apache Tomcat 9.0

## Steps
1. Clone the Repo: https://github.com/imashaindumina/w2120162-csa-coursework.git
2. Open in NetBeans: File -> Open Project -> Select SmartCampusAPI.
3. Build: Right-click the project and select Clean and Build.
4. Run: Right-click and select Run. The API will be available at http://localhost:8080/SmartCampusAPI/api/v1.

# Report: Questions and Answers

## Part 1 – Service Architecture and Setup

### 1. Project & Application Configuration
* **Question:** In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as asingleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race con[1]ditions.
* **Answer:** The default lifecycle behavior of JAX-RS resource classes is that they have a request scope, meaning that each time a new request comes in, a new object is created. However, this prevents instances from having any persistent state, which would require a Singleton DataStore where rooms and sensors can be managed. As more than one thread can potentially read and write to the same store at once, thread-safe code had to be written.

### 2. The “Discovery” Endpoint
* **Question:** Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?
* **Answer:** HATEOAS is one of the defining characteristics of sophisticated REST APIs as it allows for self-documentation through the inclusion of links to other related resources in the response. Rather than manually coding URLs for each link, developers of clients can find out what actions can be performed, like getting information on sensors in a room..

## Part 2 – Room Management

### 1. Room Resource Implementation
* **Question:** When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing
* **Answer:** It is efficient in terms of bandwidth usage when one returns only the IDs because there is less load on the network, which becomes handy in case of bigger campuses. On the other hand, it requires the client to issue individual GET requests for information about each room separately.

### 2. Room Deletion & Safety Logic
* **Question:** Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.
* **Answer:** Yes, it is idempotent. The initial DELETE request successfully deletes the room and gives back a response status code of 204. Any other subsequent request made to delete the exact room will return a status code of 404 since there’s nothing left on the server. Though the status codes have changed, the final state of the server hasn’t changed at all..

## Part 3 – Sensor Operations and Linking

### 1. Sensor Resource and Integrity
* **Question:** We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?
* **Answer:** Given that the POST method is tightly coupled with the annotation @Consumes(MediaType.APPLICATION_JSON), any input not in JSON format will be rejected by JAX-RS. The server will automatically respond with an HTTP 415 status code to any XML or plaintext request.

### 2. Filtered Retrieval & Search
* **Question:** You implemented this filtering using @QueryParam. Contrast this with an alterna[1]tive design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?
* **Answer:** Query parameters (?type=CO2) work better in terms of filtering as they make the filter a possible modifier and not a unique entity identifier. The idea behind path parameters is that they should be used to specify a unique entity. Query parameters make it easy to search for data by various criteria.

## Part 4 – Deep Nesting with Sub-Resources

### 1. The Sub-Resource Locator Pattern
* **Question:** Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?
* **Answer:** This architecture facilitates the separation of concerns by using special classes such as SensorReadingResource to handle the business logic. This ensures that one resource does not become what is referred to as a “God Class” with too much to do, thus simplifying the maintenance of the API.

## Part 5 – Advanced Error Handling & Logging

### 1. Dependency Validation (422 Unprocessable Entity)
* **Question:** Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?
* **Answer:** A HTTP 422 status code means that the server understood the request and knows about the endpoint but could not process the business logic because there is an invalid reference (such as an invalid room id). The semantic meaning of HTTP 404 error code is when the requested URI/URL could not be found.

* **Question:** From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?
* **Answer:** Stack traces, when exposed, give attackers an insight into how the system works from the inside out, with details such as class names and library versions among other things. This is important because it can help them craft attacks on weak points in the infrastructure.

* **Question:** Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?
* **Answer:** The filters have the advantage of having centralized logic, which is applicable for all requests and responses. This makes sure there is uniformity within the whole application and avoids any mistakes on the part of developers who might omit logging in certain functions.

**Developer:** Indumina Rathnayake (20240942/w2120162)
