package com.example.questease.Model.Jeu;

import static org.mockito.Mockito.*;

import android.content.Context;

import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import Service.ChoseAPI.ChoseAPI;
import Service.ChoseAPI.ChoseCallBack;
import Service.ChoseAPI.ChoseHandler;
import Service.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoseAPITest {

    @Mock
    private Context mockContext;

    @Mock
    private ChoseAPI mockChoseAPI;

    @Mock
    private Call<ChoseATrouverPrixJuste> mockCall;

    private ChoseHandler choseHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        choseHandler = new ChoseHandler(mockContext);

        // Simulez RetrofitInstance pour renvoyer mockChoseAPI
        RetrofitInstance retrofitMock = mock(RetrofitInstance.class);
        when(RetrofitInstance.getRetrofitInstance().create(ChoseAPI.class)).thenReturn(mockChoseAPI);
    }

    @Test
    public void testGetRandomChose_SuccessfulResponse() {
        // Créez une réponse fictive
        ChoseATrouverPrixJuste mockChose = new ChoseATrouverPrixJuste();
        mockChose.setValeur(1);
        Response<ChoseATrouverPrixJuste> mockResponse = Response.success(mockChose);

        // Simulez un appel Retrofit réussi
        when(mockChoseAPI.getChoseRandom()).thenReturn(mockCall);
        doAnswer(invocation -> {
            Callback<ChoseATrouverPrixJuste> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, mockResponse);
            return null;
        }).when(mockCall).enqueue(any());

        // Simulez le callback
        ChoseCallBack mockCallBack = mock(ChoseCallBack.class);

        // Exécutez la méthode testée
        choseHandler.GetRandomChose(mockCallBack);

        // Vérifiez que le callback a été appelé avec les bonnes données
        verify(mockCallBack).onChoseReceived(mockChose);
        verify(mockCallBack, never()).onFailure(anyString());
    }

    @Test
    public void testGetRandomChose_FailureResponse() {
        // Créez une réponse d'erreur fictive
        Response<ChoseATrouverPrixJuste> mockResponse = Response.error(404, null);

        // Simulez un appel Retrofit échoué
        when(mockChoseAPI.getChoseRandom()).thenReturn(mockCall);
        doAnswer(invocation -> {
            Callback<ChoseATrouverPrixJuste> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, mockResponse);
            return null;
        }).when(mockCall).enqueue(any());

        // Simulez le callback
        ChoseCallBack mockCallBack = mock(ChoseCallBack.class);

        // Exécutez la méthode testée
        choseHandler.GetRandomChose(mockCallBack);

        // Vérifiez que le callback a signalé une erreur
        verify(mockCallBack).onFailure("Request failed: 404");
        verify(mockCallBack, never()).onChoseReceived(any());
    }

    @Test
    public void testGetRandomChose_NetworkError() {
        // Simulez une exception réseau
        when(mockChoseAPI.getChoseRandom()).thenReturn(mockCall);
        doAnswer(invocation -> {
            Callback<ChoseATrouverPrixJuste> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new IOException("Network error"));
            return null;
        }).when(mockCall).enqueue(any());

        // Simulez le callback
        ChoseCallBack mockCallBack = mock(ChoseCallBack.class);

        // Exécutez la méthode testée
        choseHandler.GetRandomChose(mockCallBack);

        // Vérifiez que le callback a signalé une erreur réseau
        verify(mockCallBack).onFailure("Error: Network error");
        verify(mockCallBack, never()).onChoseReceived(any());
    }
}
