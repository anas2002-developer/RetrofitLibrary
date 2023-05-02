package com.example.retrofit.ModelResponse;

public class DeleteAccountResponse {

    String error,message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DeleteAccountResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
