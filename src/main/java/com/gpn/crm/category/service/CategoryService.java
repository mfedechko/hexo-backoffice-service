package com.gpn.crm.category.service;

import com.gpn.crm.category.dto.CategoryDto;
import com.gpn.crm.keycrm.client.KeyCrmCategoryClient;
import com.gpn.crm.keycrm.dto.KeyCrmCategory;
import com.gpn.crm.keycrm.dto.KeyCrmPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    /** KeyCRM's max page size, used to fetch every category in as few requests as possible. */
    private static final int MAX_PAGE_SIZE = 50;

    private final KeyCrmCategoryClient keyCrmCategoryClient;

    public CategoryService(KeyCrmCategoryClient keyCrmCategoryClient) {
        this.keyCrmCategoryClient = keyCrmCategoryClient;
    }

    public List<CategoryDto> getCategories() {
        return fetchAllCategories().stream().map(this::toDto).toList();
    }

    public Map<Long, CategoryDto> getCategoriesById() {
        return getCategories().stream().collect(Collectors.toMap(CategoryDto::id, Function.identity()));
    }

    private List<KeyCrmCategory> fetchAllCategories() {
        List<KeyCrmCategory> result = new ArrayList<>();

        int page = 1;
        int lastPage;
        do {
            KeyCrmPage<KeyCrmCategory> keyCrmPage = keyCrmCategoryClient.getCategories(page, MAX_PAGE_SIZE);
            result.addAll(keyCrmPage.data());

            lastPage = keyCrmPage.lastPage() == null ? page : keyCrmPage.lastPage();
            page++;
        } while (page <= lastPage);

        return result;
    }

    private CategoryDto toDto(KeyCrmCategory category) {
        return new CategoryDto(category.id(), category.name(), category.parentId());
    }
}
