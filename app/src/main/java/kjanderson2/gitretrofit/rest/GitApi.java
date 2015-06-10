package kjanderson2.gitretrofit.rest;

import kjanderson2.gitretrofit.model.GitModel;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by kjanderson2 on 6/10/15.
 * The interface for the API. User is the search term and response is the response fromt the server.
 */
public interface GitApi {
    @GET("/users/{user}") // Here is the end part of the url. {user} is filled in with search term.
    public void getFeed(@Path("user") String user, Callback<GitModel> response);
}
