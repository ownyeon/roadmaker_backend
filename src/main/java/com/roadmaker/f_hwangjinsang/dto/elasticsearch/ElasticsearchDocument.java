package com.roadmaker.f_hwangjinsang.dto.elasticsearch;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "destinations")
public class ElasticsearchDocument {

    @Id
    @Field(type = FieldType.Integer)
    private Integer destiid;
    @Field(type = FieldType.Integer)
    private Integer desticrawlid;
    @Field(type = FieldType.Double)
    private double destilatit;
    @Field(type = FieldType.Double)
    private double destilongit;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String destiaddress;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String destiappear;
    @Field(type = FieldType.Keyword, nullValue = "")
    private String desticontact;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String destidesc;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String destiholid;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String destiname;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String destiopenhr;
    @Field(type = FieldType.Text, analyzer = "nori")
    private String destiparkavail;
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date destiregdate;
    @Field(type = FieldType.Keyword)
    private String imgfname1;
    @Field(type = FieldType.Keyword)
    private String imgfname2;
    @Field(type = FieldType.Keyword)
    private String imgfname3;
    @Field(type = FieldType.Keyword)
    private String imgfname4;
    @Field(type = FieldType.Keyword)
    private String imgsname1;
    @Field(type = FieldType.Keyword)
    private String imgsname2;
    @Field(type = FieldType.Keyword)
    private String imgsname3;
    @Field(type = FieldType.Keyword)
    private String imgsname4;

}
