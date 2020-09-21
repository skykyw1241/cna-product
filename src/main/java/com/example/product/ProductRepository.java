package com.example.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.mapping.CrudMethodsSupportedHttpMethods;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
