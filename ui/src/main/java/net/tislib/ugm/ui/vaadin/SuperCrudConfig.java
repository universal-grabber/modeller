package net.tislib.ugm.ui.vaadin;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.Supplier;

@Builder
@Data
public class SuperCrudConfig<T> {
    private final Class<T> entityClass;

    private final Supplier<List<T>> recordsProvider;
}
