package com.example.jeyasha.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.jeyasha.model.Upload;
import com.example.jeyasha.repository.UploadRepository;

@Service
public class UploadService {

	@Autowired
	UploadRepository uploadRepository;

	public ResponseEntity<Upload> createUpload(Upload upload) {
		try {
			Integer id = uploadRepository.getMaxUploadId() + 1;
			Upload newUpload = uploadRepository.save(new Upload(id,upload.getTitle(),upload.getArtistName(),upload.getMedium(),upload.getCategory(),upload.getSize(),upload.getDescription(),upload.getPrice(),upload.getImage()));
			return new ResponseEntity<>(newUpload,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	
	public ResponseEntity<Upload> getUploadById(int id) {
		try {
			Optional<Upload> upload = uploadRepository.findById(id);
			if(upload.isPresent()) {
				return new ResponseEntity<>(upload.get(),HttpStatus.OK);
			}
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	public ResponseEntity<Map<String, Object>> getAllUpload(int pageNo ,int pageSize) {
		try {
            Map<String, Object> response = new HashMap<>();
            Sort sort = Sort.by(Sort.Direction.DESC,"id");
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            Page<Upload> page = uploadRepository.findAll(pageable);
            response.put("data", page.getContent());
            response.put("Total_No_Of_Pages", page.getTotalPages());
            response.put("Total_No_Of_Elements", page.getTotalElements());
            response.put("Current page no", page.getNumber());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}


	public ResponseEntity<Upload> deleteUpload(int id) {
		try {
			Optional<Upload> uploadData = uploadRepository.findById(id);
			if(uploadData.isPresent()) {
				uploadRepository.deleteById(id);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}


	public ResponseEntity<Upload> updateUpload(int id, Upload upload) {
		try {
			Optional<Upload> uploadData = uploadRepository.findById(id);
			if(uploadData.isPresent()) {
				Upload _upload = uploadData.get();
				_upload.setTitle(upload.getTitle());				
				_upload.setArtistName(upload.getArtistName());				
				_upload.setMedium(upload.getMedium());				
				_upload.setCategory(upload.getCategory());				
				_upload.setSize(upload.getSize());				
				_upload.setDescription(upload.getDescription());
				_upload.setPrice(upload.getPrice());
				_upload.setImage(upload.getImage());
				return new ResponseEntity<>(uploadRepository.save(_upload),HttpStatus.OK);
			}
			return new ResponseEntity<> (HttpStatus.NOT_FOUND);
		}catch(Exception e) {
        	return new ResponseEntity<>(null,HttpStatus.SERVICE_UNAVAILABLE);
        }
	}	
	
	public ResponseEntity<Map<String, Object>> searchedUpload(String searched, int pageNo, int pageSize) {
		
		List<Upload> searchedUpload = uploadRepository.findBySearchContaining(searched);
		Map<String, Object> response = new HashMap<>();
		PagedListHolder<Upload> page = new PagedListHolder<Upload>(searchedUpload);
		page.setPageSize(pageSize); // number of items per page
		page.setPage(pageNo); 
		
		response.put("data", page.getPageList());
		response.put("Total_No_Of_Elements", page.getNrOfElements());
		response.put("Total_No_Of_Pages", page.getPage());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	
	public ResponseEntity<Map<String, Object>> getFilteredUpload(String medium, String category, int minPrice,
			int maxPrice, int pageNo, int pageSize) {

		List<Upload> filteredUpload = uploadRepository.findByFilter(medium,category,minPrice,maxPrice);
		Map<String, Object> response = new HashMap<>();
		PagedListHolder<Upload> page = new PagedListHolder<Upload>(filteredUpload);
		page.setPageSize(pageSize); // number of items per page
		page.setPage(pageNo); 
		response.put("data", page.getPageList());
		response.put("Total_No_Of_Pages", page.getPage());
		response.put("Total_No_Of_Elements", page.getNrOfElements());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}

