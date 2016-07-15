package com.smartwg.core.internal.domain.dtos;

/**
 * @author Kamil Sierzant (ks)
 */
public class ListItemDTO {

  private Integer id;
  private String name;
  private CategoryDTO category;
  private int amount;
  private boolean done;
  private boolean deleted;

  /**
   * constructor for orm
   */
  public ListItemDTO(final Integer id, final String name, final Integer categoryId,
      final String categoryName, final boolean categoryIsFixedCost,
      final boolean categoryIsDefault, final int amount, final boolean done) {
    this.id = id;
    this.name = name;
    this.done = done;
    this.category =
        new CategoryDTO(categoryId, categoryName, categoryIsFixedCost, categoryIsDefault);
    this.amount = amount;
  }

  public ListItemDTO(final Integer id, final String name, final CategoryDTO category,
      final int amount, final boolean done) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.amount = amount;
    this.done = done;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public boolean isDone() {
    return done;
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public String toString() {
    return "ListItemDTO{" + "id=" + id + ", name='" + name + '\'' + ", category=" + category
        + ", amount=" + amount + ", done=" + done + ", deleted=" + deleted + '}';
  }
}
