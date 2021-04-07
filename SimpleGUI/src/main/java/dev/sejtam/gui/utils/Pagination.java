package dev.sejtam.gui.utils;

import lombok.AccessLevel;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pagination<T> extends ArrayList<T> {

    private int pageSize;

    @Setter(AccessLevel.PUBLIC)
    protected T onClickAdd;

    public Pagination(int pageSize) {
        this(pageSize, new ArrayList<>());
    }

    @SafeVarargs
    public Pagination(int pageSize, T... objects) {
        this(pageSize, Arrays.asList(objects));
    }

    public Pagination(int pageSize, List<T> objects) {
        this.pageSize = pageSize;
        addAll(objects);
    }

    public int pageSize() {
        return this.pageSize;
    }

    public int totalPages() {
        return (int) Math.ceil((double) (size() + (this.onClickAdd == null ? 0 : 1)) / this.pageSize);
    }

    public boolean exists(int page) {
        return !(page < 0) && page < totalPages();
    }

    public List<T> getPage(int page) {
        List<T> objects = new ArrayList<>();

        if (size() == 0) {
            if (onClickAdd != null)
                objects.add(onClickAdd);

            return objects;
        }

        if (page < 0 || page >= totalPages())
            throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + totalPages());

        int min = page * this.pageSize;
        int max = ((page * this.pageSize) + this.pageSize);

        if (max > size()) max = size();

        for (int i = min; max > i; i++) {
            objects.add(get(i));
        }

        if (this.onClickAdd != null) {
            if (objects.size() < pageSize) {
                objects.add(this.onClickAdd);
            }
        }

        return objects;
    }
}