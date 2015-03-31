package com.taptech.spoonscore.repository;

import com.taptech.spoonscore.entity.RestaurantScore;
import com.taptech.spoonscore.entity.ZipCodes;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by tap on 3/30/15.
 */
public interface RestaurantScoreRepository  extends JpaRepository<RestaurantScore, Long> {
    RestaurantScore findOneByRestaurantIdAndFoundBy(String restaurantId, String foundBy);
}
