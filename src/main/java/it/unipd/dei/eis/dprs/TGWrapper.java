package it.unipd.dei.eis.dprs;

public class TGWrapper {
    private TGResponse[] response;

    public TGWrapper()  {
    }

    public TGWrapper(final TGResponse[] response) {
        this.response = response;
    }

    public TGResponse[] getResponse() {
        return response;
    }
}