package com.udacity.pricing.domain.price;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends PagingAndSortingRepository<Price, Long> {

}
