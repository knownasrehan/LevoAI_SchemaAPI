# Schema API

A robust Spring Boot microservice for uploading, versioning, and retrieving OpenAPI schemas with support for both application-level and service-level schema management.

## 🚀 Features

- **Schema Upload**: Upload OpenAPI schemas in JSON or YAML format
- **Automatic Versioning**: Automatically version schemas on each upload
- **Application-Level Management**: Manage schemas at the application level
- **Service-Level Management**: Organize schemas by individual services within applications
- **Version Retrieval**: Access specific versions of schemas
- **Format Support**: Compatible with both JSON and YAML OpenAPI specifications

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Spring Boot 3.x

## 🛠️ Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd LevoAI_SchemaAPI
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The service will start on `http://localhost:8080` by default.

## 📚 API Endpoints

### Upload Schema
**POST** `/schemas/upload`

Upload a new OpenAPI schema file. The system automatically assigns version numbers incrementally.

**Form Parameters:**
- `application` (required) - Application name
- `service` (optional) - Service name for service-specific schemas
- `file` (required) - OpenAPI schema file (JSON/YAML)

**Response:**
- `201 Created` - Schema uploaded successfully
- `400 Bad Request` - Invalid file format or missing parameters

---

### Get Latest Application Schema
**GET** `/schemas/{application}`

Retrieve the latest version of an application's schema.

**Path Parameters:**
- `application` - Application name

**Response:**
- `200 OK` - Returns the latest schema
- `404 Not Found` - Application not found

---

### Get Latest Service Schema
**GET** `/schemas/{application}/services/{service}`

Retrieve the latest version of a specific service's schema within an application.

**Path Parameters:**
- `application` - Application name
- `service` - Service name

**Response:**
- `200 OK` - Returns the latest service schema
- `404 Not Found` - Application or service not found

---

### Get Specific Application Version
**GET** `/schemas/{application}/versions/{version}`

Retrieve a specific version of an application's schema.

**Path Parameters:**
- `application` - Application name
- `version` - Version number

**Response:**
- `200 OK` - Returns the requested schema version
- `404 Not Found` - Application or version not found

---

### Get Specific Service Version
**GET** `/schemas/{application}/services/{service}/versions/{version}`

Retrieve a specific version of a service's schema.

**Path Parameters:**
- `application` - Application name
- `service` - Service name
- `version` - Version number

**Response:**
- `200 OK` - Returns the requested service schema version
- `404 Not Found` - Application, service, or version not found

## 💡 Usage Examples

### 1. Upload Application-Level Schemas

Upload different formats to see automatic versioning in action:

```bash
# Upload first version (JSON format)
curl -X POST "http://localhost:8080/schemas/upload" \
  -F "application=sampleApp" \
  -F "file=@openapi.json"

# Upload second version (YAML format)
curl -X POST "http://localhost:8080/schemas/upload" \
  -F "application=sampleApp" \
  -F "file=@openapi.yaml"

# Upload third version
curl -X POST "http://localhost:8080/schemas/upload" \
  -F "application=sampleApp" \
  -F "file=@updated-schema.json"
```

### 2. Upload Service-Level Schema

```bash
# Upload schema for a specific service
curl -X POST "http://localhost:8080/schemas/upload" \
  -F "application=sampleApp" \
  -F "service=userService" \
  -F "file=@user-service-openapi.json"
```

### 3. Retrieve Schemas

```bash
# Get latest application schema
curl "http://localhost:8080/schemas/sampleApp"

# Get specific version of application schema
curl "http://localhost:8080/schemas/sampleApp/versions/1"
curl "http://localhost:8080/schemas/sampleApp/versions/2"

# Get latest service schema
curl "http://localhost:8080/schemas/sampleApp/services/userService"

# Get specific version of service schema
curl "http://localhost:8080/schemas/sampleApp/services/userService/versions/1"
```

### 4. Complete Workflow Example

```bash
# 1. Upload initial application schema
curl -X POST "http://localhost:8080/schemas/upload" \
  -F "application=ecommerce" \
  -F "file=@ecommerce-v1.json"

# 2. Upload service-specific schemas
curl -X POST "http://localhost:8080/schemas/upload" \
  -F "application=ecommerce" \
  -F "service=payment" \
  -F "file=@payment-service.yaml"

curl -X POST "http://localhost:8080/schemas/upload" \
  -F "application=ecommerce" \
  -F "service=inventory" \
  -F "file=@inventory-service.json"

# 3. Retrieve schemas
curl "http://localhost:8080/schemas/ecommerce"                    # Latest app schema
curl "http://localhost:8080/schemas/ecommerce/services/payment"   # Latest payment service
curl "http://localhost:8080/schemas/ecommerce/versions/1"         # First app version
```

## 📄 Response Format

### Successful Upload Response
```
Schema uploaded successfully
```

### Schema Retrieval Response
The API returns the OpenAPI schema content directly in the original format (JSON/YAML).

### Error Response
```
{
   "timestamp": "2025-09-04T21:22:56.649+00:00",
   "status": 404,
   "error": "Not found",
   "path": "/schemas/sampleApp/services/userService/1"
}
```

## 🛡️ Error Handling

The API handles various error scenarios:

- **Invalid file format**: Returns 400 with descriptive error message
- **Missing required parameters**: Returns 400 with validation details
- **Schema not found**: Returns 404 with resource information
- **File upload errors**: Returns 500 with technical details

## 📝 Notes

- Version numbers start from 1 and increment automatically
- Both JSON and YAML OpenAPI formats are supported
- Schemas are validated for OpenAPI compliance before storage
- Service-level schemas are independent of application-level schemas
