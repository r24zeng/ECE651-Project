package android.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new ca.uwaterloo.newsapp.DataBinderMapperImpl());
  }
}
