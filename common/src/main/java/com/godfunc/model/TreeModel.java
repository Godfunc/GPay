package com.godfunc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * @author godfunc
 * @email godfunc@outlook.com
 */
@Data
public class TreeModel<T> implements Serializable {

    private Long id;

    private Long pid;

    private List<T> children = new ArrayList<>();
}
