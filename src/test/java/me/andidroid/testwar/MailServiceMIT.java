package me.andidroid.testwar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.microshed.testing.jaxrs.RESTClient;
import org.microshed.testing.jupiter.MicroShedTest;
import org.microshed.testing.SharedContainerConfig;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.junit.jupiter.Container;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.containsString;
import static io.restassured.http.ContentType.TEXT;

@Tag("MicroShedTest")
@MicroShedTest
@SharedContainerConfig(MicroshedSharedContainerConfiguration.class)
public class MailServiceMIT
{
    
    @Test
    public void testGetMail()
    {
        
        String host = MicroshedSharedContainerConfiguration.greenMailContainer.getHost();
        System.out.println(host);
        // SMTPS : 3465, api : 8080
        // Integer port = MicroshedSharedContainerConfiguration.greenMailContainer.getMappedPort(8080);
        // System.out.println(port);
        
        // Response response = given().baseUri("http://" + host).basePath("/api").port(port).auth().preemptive().basic("test", "test").when().get("/configuration");
        // System.out.println(response.asPrettyString());
        
        // response = given().baseUri("http://" + host).port(port).auth().preemptive().basic("test", "test").when().get("/greenmail-openapi.yml");
        // System.out.println(response.asPrettyString());
        
        /*
         * {
         * "serverSetups": [
         * {
         * "port": 3465,
         * "address": "0.0.0.0",
         * "protocol": "smtps",
         * "isSecure": true,
         * "readTimeout": -1,
         * "writeTimeout": -1,
         * "connectionTimeout": -1,
         * "serverStartupTimeout": 2000,
         * "isDynamicPort": false
         * }
         * ],
         * "authenticationDisabled": false
         * }
         * ---
         * openapi: "3.0.0"
         * info:
         * version: "1.6.5"
         * title: GreenMail OpenAPI
         * description: <p>RESTful API for GreenMail</p>
         * license:
         * name: Apache 2.0
         * url: "https://www.apache.org/licenses/LICENSE-2.0.html"
         * paths:
         * /api/configuration:
         * get:
         * description: Gets current GreenMail configuration
         * operationId: get_configuration
         * responses:
         * '200':
         * description: Current configuration
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/Configuration"
         * default:
         * description: Unexpected error
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/ErrorResponse"
         * /api/user:
         * get:
         * description: Gets current GreenMail users
         * operationId: get_all_user
         * responses:
         * '200':
         * description: List of users
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/Users"
         * default:
         * description: Unexpected error
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/ErrorResponse"
         * post:
         * description: Creates a new GreenMail users
         * operationId: create_user
         * requestBody:
         * required: true
         * content:
         * application/json:
         * schema:
         * type: object
         * required:
         * - email
         * - login
         * - password
         * properties:
         * email:
         * type: string
         * login:
         * type: string
         * password:
         * type: string
         * responses:
         * '200':
         * description: Newly created user
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/User"
         * default:
         * description: Unexpected error
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/ErrorResponse"
         * /api/user/{id}:
         * parameters:
         * - in: path
         * name: id
         * description: Either user login or user email as identifier
         * required: true
         * schema:
         * type: string
         * delete:
         * description: Deletes given GreenMail user
         * operationId: delete_user
         * responses:
         * '200':
         * description: Success
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/SuccessResponse"
         * '400':
         * description: User not found
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/ErrorResponse"
         * /api/service/readiness:
         * get:
         * description: Checks GreenMail readiness (if service up and available)
         * operationId: readiness
         * responses:
         * '200':
         * description: Service is ready
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/SuccessResponse"
         * '503':
         * description: Service is not ready
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/SuccessResponse"
         * default:
         * description: Unexpected error
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/ErrorResponse"
         * /api/service/reset:
         * post:
         * description: Restarts GreenMail using current configuration.
         * operationId: reset
         * responses:
         * '200':
         * description: Success
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/SuccessResponse"
         * default:
         * description: Unexpected error
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/ErrorResponse"
         * /api/mail/purge:
         * post:
         * description: Purges all mails
         * operationId: purge
         * responses:
         * '200':
         * description: Success
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/SuccessResponse"
         * default:
         * description: Unexpected error
         * content:
         * application/json:
         * schema:
         * $ref: "#/components/schemas/ErrorResponse"
         * components:
         * schemas:
         * Configuration:
         * type: object
         * properties:
         * defaultHostname:
         * type: string
         * portOffset:
         * type: integer
         * format: int32
         * serverSetups:
         * type: array
         * items:
         * $ref: "#/components/schemas/ServerSetup"
         * required:
         * - serviceConfigurations
         * Users:
         * type: "array"
         * description: Configured users for authentication
         * items:
         * $ref: "#/components/schemas/User"
         * User:
         * type: object
         * required:
         * - login
         * - email
         * properties:
         * login:
         * type: string
         * email:
         * type: string
         * ServerSetup:
         * type: object
         * properties:
         * port:
         * description: Service port
         * type: integer
         * format: int32
         * address:
         * description: Service address
         * type: string
         * protocol:
         * description: Service protocol for receiving or sending emails.
         * type: string
         * enum:
         * - pop3
         * - pop3s
         * - imap
         * - imaps
         * - smtp
         * - smtps
         * isSecure:
         * description: Flag if secure transport (TLS) should be enabled
         * type: boolean
         * readTimeout:
         * description: Service socket read timeout in ms
         * type: integer
         * format: int32
         * writeTimeout:
         * description: Service socket write timeout in ms
         * type: integer
         * format: int32
         * connectionTimeout:
         * description: Service socket connection timeout in ms
         * type: integer
         * format: int32
         * serverStartupTimeout:
         * description: Service startup timeout in ms
         * type: integer
         * format: int32
         * isDynamicPort:
         * description: Flag if port should be automatically determined
         * type: boolean
         * required:
         * - address
         * - port
         * - protocol
         * - isSecure
         * ErrorResponse:
         * properties:
         * message:
         * type: string
         * required:
         * - message
         * SuccessResponse:
         * properties:
         * message:
         * type: string
         * required:
         * - message
         */
    }
}
