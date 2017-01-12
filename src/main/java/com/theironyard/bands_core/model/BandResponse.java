package com.theironyard.bands_core.model;

import java.util.List;

/**
 * Created by alhanger on 1/12/17.
 */
public class BandResponse {

    private String statusCode;
    private String statusMessage;
    private Band band;
    private List<Band> bands;
    private String detailMessage;

    public BandResponse(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public BandResponse(String statusCode, String statusMessage, Band band) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.band = band;
    }

    public BandResponse(String statusCode, String statusMessage, List<Band> bands) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.bands = bands;
    }

    public BandResponse(String statusCode, String statusMessage, String detailMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.detailMessage = detailMessage;
    }

    public BandResponse(String statusCode, String statusMessage, Band band, String detailMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.band = band;
        this.detailMessage = detailMessage;
    }

    public BandResponse(String statusCode, String statusMessage, List<Band> bands, String detailMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.bands = bands;
        this.detailMessage = detailMessage;
    }

}
