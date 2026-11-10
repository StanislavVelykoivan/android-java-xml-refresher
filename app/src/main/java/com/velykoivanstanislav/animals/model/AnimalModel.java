package com.velykoivanstanislav.animals.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AnimalModel implements Parcelable {
    public String name;
    public Taxonomy taxonomy;
    public String location;
    public Speed speed;
    public String diet;
    @SerializedName("lifespan")
    public String lifeSpan;
    @SerializedName("image")
    public String imageUrl;


    public AnimalModel(String name, Taxonomy taxonomy, String location, Speed speed, String diet, String lifeSpan, String imageUrl) {
        this.name = name;
        this.taxonomy = taxonomy;
        this.location = location;
        this.speed = speed;
        this.diet = diet;
        this.lifeSpan = lifeSpan;
        this.imageUrl = imageUrl;
    }

    protected AnimalModel(Parcel in) {
        name = in.readString();
        taxonomy = in.readParcelable(Taxonomy.class.getClassLoader());
        location = in.readString();
        speed = in.readParcelable(Speed.class.getClassLoader());
        diet = in.readString();
        lifeSpan = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(taxonomy, flags);
        dest.writeString(location);
        dest.writeParcelable(speed, flags);
        dest.writeString(diet);
        dest.writeString(lifeSpan);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnimalModel> CREATOR = new Creator<AnimalModel>() {
        @Override
        public AnimalModel createFromParcel(Parcel in) {
            return new AnimalModel(in);
        }

        @Override
        public AnimalModel[] newArray(int size) {
            return new AnimalModel[size];
        }
    };
}

class Taxonomy implements Parcelable {
    String kingdom;
    String order;
    String family;

    protected Taxonomy(Parcel in) {
        kingdom = in.readString();
        order = in.readString();
        family = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kingdom);
        dest.writeString(order);
        dest.writeString(family);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Taxonomy> CREATOR = new Creator<Taxonomy>() {
        @Override
        public Taxonomy createFromParcel(Parcel in) {
            return new Taxonomy(in);
        }

        @Override
        public Taxonomy[] newArray(int size) {
            return new Taxonomy[size];
        }
    };
}

class Speed implements Parcelable {
    String metric;
    String imperial;

    protected Speed(Parcel in) {
        metric = in.readString();
        imperial = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(metric);
        dest.writeString(imperial);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Speed> CREATOR = new Creator<Speed>() {
        @Override
        public Speed createFromParcel(Parcel in) {
            return new Speed(in);
        }

        @Override
        public Speed[] newArray(int size) {
            return new Speed[size];
        }
    };
}