package com.example.jeyasha.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.jeyasha.model.Upload;


@Repository
public interface UploadRepository extends MongoRepository<Upload, Long>{
	
	
	@Query("{$or: [ { 'title': { $regex: ?0 , $options: 'i' } }, { 'artistName':{ $regex: ?0, $options: 'i' } },{ 'medium':{ $regex: ?0, $options: 'i' } },{ 'category':{ $regex: ?0, $options: 'i' } },{ 'description': { $regex: ?0 , $options: 'i' } } ]}")
	List<Upload> findBySearchContaining(String searchString);

	@Query("{$and: [{'medium' : { $regex: ?0}},{'category':{$regex:?1}},{'price':{$gt:?2,$lt:?3}}]}")
	List<Upload> findByFilter(String medium ,String category,int minPrice, int maxPrice);
	
	public Integer getMaxUploadId();

	Optional<Upload> findById(int id);

}