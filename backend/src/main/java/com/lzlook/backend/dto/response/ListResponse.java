package com.lzlook.backend.dto.response;

import java.util.List;

public class ListResponse<T> extends Response {

    private List<T> list;

    public ListResponse() {
    }

    public ListResponse(List<T> list) {
        this.setCode(Response.SUCCESS);
        this.list = list;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list= list;
    }
}
