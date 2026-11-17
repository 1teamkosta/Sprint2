package kr.swdl.sprint2.service.pagination;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import kr.swdl.sprint2.service.properties.PageProperties;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class Pagination {
    public static <T> List<T> setPage(int page, int pageSize, String sortType, String sortDir, Supplier<List<T>> list) {
        PageHelper.startPage(page, pageSize);
        PageHelper.orderBy(sortType + " " + sortDir);

        return new PageInfo<>(list.get()).getList();
    }
}
