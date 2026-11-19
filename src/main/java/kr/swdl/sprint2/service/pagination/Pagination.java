package kr.swdl.sprint2.service.pagination;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Supplier;

public final class Pagination {
    private Pagination() {}

    public static <T> List<T> execute(int page, int pageSize, String sortType, String sortDir, Supplier<List<T>> list) {
        PageHelper.startPage(page, pageSize);
        PageHelper.orderBy(sortType + " " + sortDir);

        return new PageInfo<>(list.get()).getList();
    }
}
