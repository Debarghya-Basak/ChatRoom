package com.dbtapps.chatroom.models;

public class PermissionModel {

    public String permissionName;
    public String permission;
    public int permissionMinBuildVersion;
    public int permissionMaxBuildVersion;

    public PermissionModel(String permissionName, String permission, int permissionMinBuildVersion, int permissionMaxBuildVersion) {
        this.permissionName = permissionName;
        this.permission = permission;
        this.permissionMinBuildVersion = permissionMinBuildVersion;
        this.permissionMaxBuildVersion = permissionMaxBuildVersion;
    }

    public PermissionModel(String permissionName, String permission, int permissionMinBuildVersion) {
        this.permissionName = permissionName;
        this.permission = permission;
        this.permissionMinBuildVersion = permissionMinBuildVersion;
        this.permissionMaxBuildVersion = 100;
    }
}
