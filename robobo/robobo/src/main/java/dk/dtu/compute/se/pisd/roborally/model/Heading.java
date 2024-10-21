  package dk.dtu.compute.se.pisd.roborally.model;


public enum Heading {

    SOUTH, WEST, NORTH, EAST;

    public Heading next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Heading prev() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }
    public static Heading get(String heading) {
        for(Heading heading1 : values()) {
            if (heading1.toString().equals(heading)) {
                return heading1;
            }
        }
        return SOUTH;
    }
}
