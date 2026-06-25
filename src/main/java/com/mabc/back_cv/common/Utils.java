package com.mabc.back_cv.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


public class Utils{

    public static Pageable createPageable(Integer page, Integer size){
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size < 1) ? 10 : size;
        return PageRequest.of(page, size);
    }
    
    public static Pageable createPageable(Integer page, Integer size, String orderBy){
        page = (page == null || page < 0) ? 0 : page;
        size = (size == null || size < 1) ? 10 : size;
        return PageRequest.of(page, size, Sort.by(orderBy));
    }

}