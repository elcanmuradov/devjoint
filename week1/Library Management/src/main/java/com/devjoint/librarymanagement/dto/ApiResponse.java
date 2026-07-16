package com.devjoint.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T>{
    private T data;

    private boolean success;

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<T>(data,true);
    }


    public static <T> ApiResponse<T> fail(T data){
        return new ApiResponse<T>(data,false);
    }
}
