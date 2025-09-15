package com.vietlong.spring_app.service;

import com.vietlong.spring_app.dto.request.CreateCategoryRequest;
import com.vietlong.spring_app.dto.request.UpdateCategoryRequest;
import com.vietlong.spring_app.dto.response.CategoryResponse;
import com.vietlong.spring_app.exception.AppException;
import com.vietlong.spring_app.exception.ErrorCode;
import com.vietlong.spring_app.model.Category;
import com.vietlong.spring_app.repository.CategoryRepository;
import com.vietlong.spring_app.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final ServiceRepository serviceRepository;

    public AdminCategoryService(CategoryRepository categoryRepository, ServiceRepository serviceRepository) {
        this.categoryRepository = categoryRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) throws AppException {

        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Tên danh mục đã tồn tại");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(true)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        List<CategoryResponse> responseList = new ArrayList<>();
        for (Category category : categories) {
            responseList.add(CategoryResponse.fromEntity(category));
        }
        return responseList;
    }

    public CategoryResponse getCategoryById(String categoryId) throws AppException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public CategoryResponse updateCategory(String categoryId, UpdateCategoryRequest request) throws AppException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByNameAndIdNot(request.getName(), categoryId)) {
                throw new AppException(ErrorCode.INVALID_REQUEST, "Tên danh mục đã tồn tại");
            }
            category.setName(request.getName());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(updatedCategory);
    }

    @Transactional
    public void deleteCategory(String categoryId) throws AppException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (serviceRepository.existsByCategory(category)) {
            throw new AppException(ErrorCode.CATEGORY_IN_USE);
        }

        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryResponse activateCategory(String categoryId) throws AppException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.activate();
        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(updatedCategory);
    }

    @Transactional
    public CategoryResponse deactivateCategory(String categoryId) throws AppException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.deactivate();
        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(updatedCategory);
    }
}
