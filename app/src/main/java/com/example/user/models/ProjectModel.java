package com.example.user.models;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectModel
{



    public String id;

    public String projectName;

    public String projectCode;

    public String managerName;




}
