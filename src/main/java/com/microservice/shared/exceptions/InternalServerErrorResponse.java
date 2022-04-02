package com.microservice.shared.exceptions;

public record InternalServerErrorResponse(int status, String message, String timestamp) {
}
