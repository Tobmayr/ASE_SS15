package com.smartwg.core.internal.domain.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.smartwg.core.controllers.SmartWG;
import com.smartwg.core.internal.domain.entities.Category;
import com.smartwg.core.util.Constants;

/**
 * @author Kamil Sierzant (ks)
 */
public class CategoryDTO {

    private ResourceBundle ms = SmartWG.getMessageBundle();

    private Integer id;
    private String name;
    private Integer group_id;
    private boolean fixedCost, defaultCategory, deleted;
    private CategoryDTO parentCategory;
    private List<UserGroupCategoryDTO> userGroupCategories = new ArrayList<UserGroupCategoryDTO>();
    private boolean customDivision;
    private String i18NKey;

    public CategoryDTO() {
    }

    public CategoryDTO(Integer id, String name, boolean isFixedCost, boolean isDefault) {
        this.id = id;
        this.fixedCost = isFixedCost;
        this.defaultCategory = isDefault;
        this.name = name;
        if (defaultCategory) {
            i18NKey = name;
            this.name = ms.getString(i18NKey);
        }
    }

    public CategoryDTO(Category category) {
        if (category == null) {
            return;
        }
        if (category.getId() != null) {
            this.id = category.getId();
        }
        this.name = category.getName();

        if (category.getGroup() != null) {
            this.group_id = category.getGroup().getId();
        }
        this.fixedCost = category.isFixedCost();
        if (category.getParentCategory() != null) {
            this.parentCategory = new CategoryDTO(category.getParentCategory());
        }
        if (category.getUserGroupCategories() != null && !category.getUserGroupCategories().isEmpty()) {
            this.customDivision = true;
        }
        this.defaultCategory = category.isDefaultCategory();
        if (this.defaultCategory) {
            i18NKey = name;
            this.name = ms.getString(i18NKey);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CategoryDTO))
            return false;
        CategoryDTO that = (CategoryDTO) o;
        return new EqualsBuilder().append(this.id, that.id).append(this.name, that.name)
                .append(this.group_id, that.group_id).append(this.fixedCost, that.fixedCost)
                .append(this.defaultCategory, that.defaultCategory)
                .append(this.parentCategory, that.parentCategory)
                .append(this.customDivision, that.customDivision).isEquals();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getI18NKey() {
        return this.i18NKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public boolean isDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(boolean defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public boolean isFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(boolean fixedCost) {
        this.fixedCost = fixedCost;
    }

    public CategoryDTO getParentCategory() {
        return parentCategory;
    }

    public boolean isCustomDivision() {
        return customDivision;
    }

    public void setCustomDivision(boolean customDivision) {
        this.customDivision = customDivision;
    }

    public void setParentCategory(CategoryDTO parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<UserGroupCategoryDTO> getUserGroupCategories() {
        return userGroupCategories;
    }

    public void setUserGroupCategories(List<UserGroupCategoryDTO> userGroupCategories) {
        this.userGroupCategories = userGroupCategories;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
