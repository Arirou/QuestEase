package com.example.questease.TestUnitaire;

import static org.mockito.Mockito.*;

import android.content.Context;

import com.example.questease.Model.BDD.MotCryptex;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import Service.CryptexAPI.HandlerMotCryptexAPI;
import Service.CryptexAPI.MotCryptexAPI;
import Service.CryptexAPI.MotCryptexCallback;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandlerMotCryptexAPITest {

    private HandlerMotCryptexAPI handlerMotCryptexAPI;
    private MotCryptexAPI motCryptexAPI; // Mocked API interface
    private Context mockContext; // Mocked context
    private MotCryptexCallback mockCallback; // Mocked callback
    private Call<MotCryptex> mockCall; // Mocked retrofit Call

    @Before
    public void setUp() {
        // Mock dependencies
        mockContext = mock(Context.class);
        motCryptexAPI = mock(MotCryptexAPI.class);
        mockCallback = mock(MotCryptexCallback.class);
        mockCall = mock(Call.class);

        // Inject the mocked API into the Handler
        handlerMotCryptexAPI = new HandlerMotCryptexAPI(mockContext, motCryptexAPI);
    }

    @Test
    public void testFetchData_successfulResponse() {
        // Mock a successful response
        MotCryptex mockMotCryptex = new MotCryptex(1, "modus", 1, 1); // Replace with relevant fields
        Response<MotCryptex> mockResponse = Response.success(mockMotCryptex);

        // Stub the API call
        when(motCryptexAPI.getMotCryptexById(anyInt())).thenReturn(mockCall);

        // Simulate enqueue calling onResponse with the mocked response
        doAnswer(invocation -> {
            Callback<MotCryptex> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, mockResponse);
            return null;
        }).when(mockCall).enqueue(any());

        // Call the method under test
        handlerMotCryptexAPI.fetchData(1, mockCallback);

        // Verify the callback is called with the correct data
        verify(mockCallback).onMotCryptexReceived(mockMotCryptex);
        verify(mockCallback, never()).onFailure(anyString());
    }

    @Test
    public void testFetchData_failedResponse() {
        // Mock a failed response
        ResponseBody errorBody = ResponseBody.create("Error message", MediaType.get("application/json"));
        Response<MotCryptex> mockResponse = Response.error(404, errorBody);

        // Stub the API call
        when(motCryptexAPI.getMotCryptexById(anyInt())).thenReturn(mockCall);

        // Simulate enqueue calling onResponse with the mocked failed response
        doAnswer(invocation -> {
            Callback<MotCryptex> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, mockResponse);
            return null;
        }).when(mockCall).enqueue(any());

        // Call the method under test
        handlerMotCryptexAPI.fetchData(1, mockCallback);

        // Verify the failure callback is called
        verify(mockCallback).onFailure("Request failed: 404");
        verify(mockCallback, never()).onMotCryptexReceived(any());
    }

    @Test
    public void testFetchData_networkError() {
        // Mock a network error
        Throwable mockThrowable = new Throwable("Network error");

        // Stub the API call
        when(motCryptexAPI.getMotCryptexById(anyInt())).thenReturn(mockCall);

        // Simulate enqueue calling onFailure with the mocked throwable
        doAnswer(invocation -> {
            Callback<MotCryptex> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, mockThrowable);
            return null;
        }).when(mockCall).enqueue(any());

        // Call the method under test
        handlerMotCryptexAPI.fetchData(1, mockCallback);

        // Verify the failure callback is called with the correct error message
        verify(mockCallback).onFailure("Error: Network error");
        verify(mockCallback, never()).onMotCryptexReceived(any());
    }
}
