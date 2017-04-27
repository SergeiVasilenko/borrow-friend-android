package vision.genesis.network;


import java.math.BigDecimal;
import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vision.genesis.model.Ask;
import vision.genesis.model.Balance;
import vision.genesis.model.Offer;
import vision.genesis.model.StatusResponse;
import vision.genesis.model.UsersIntersects;

public interface Api {

	@GET("data/GetAsks")
	Observable<Result<ArrayList<Ask>>> getAsks(@Query("userId") long vkId);

	@GET("data/GetOffers")
	Observable<Result<ArrayList<Offer>>> getOffers(@Query("userId") long vkId);

	@GET("operations/AddOrder")
	Observable<Result<StatusResponse>> postOrder(@Query("userId") long vkId,
												 @Query("orderType") String orderType,
												 @Query("amount") BigDecimal amount,
												 @Query("percentPerDay") BigDecimal percentPerDay,
												 @Query("durationMax") int durationMax,
												 @Query("durationMin") int durationMin,
												 @Query("title") String title);

	@GET("operations/MakeDeal")
	Observable<Result<StatusResponse>> postDeal(@Query("userId") long vkId,
												@Query("askId") long askId);

	@GET("Balance/GetBalance")
	Observable<Result<Balance>> getBalance(@Query("userId") long vkId);

	// http://52.164.212.9/api/vk/GetFriends?id=295222
	@GET("vk/GetFriends")
	Observable<Result<ResponseBody>> getFriends(@Query("userId") long vkId);

	@GET("Balance/AddBalance")
	Observable<Result<ResponseBody>> addBalance(@Query("userId") long vkId,
												@Query("amount") int amount);

	@GET("Balance/WithdrawBalance")
	Observable<Result<ResponseBody>> withdrawBalance(@Query("userId") long vkId,
													 @Query("amount") int amount);

	@GET("vk/GetUsersIntersect")
	Observable<Result<UsersIntersects>> getUsersIntersect(@Query("first") long firstVkId,
														  @Query("second") long secondVkId);
}

