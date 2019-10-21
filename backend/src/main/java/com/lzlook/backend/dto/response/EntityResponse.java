package com.lzlook.backend.dto.response;

public class EntityResponse<E> extends Response {

    private E entity;

    public EntityResponse() {
    }

    public EntityResponse(E entity) {
        this.entity = entity;
    }

    public EntityResponse(int code, String msg, E entity) {
        this.setCode(code);
        this.setMsg(msg);
        this.entity = entity;
    }

    public EntityResponse(int code, String msg) {
        this.setCode(code);
        this.setMsg(msg);
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    public void success(E entity) {
        super.success();
        this.entity = entity;
    }
}
