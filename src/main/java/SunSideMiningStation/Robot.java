package SunSideMiningStation;

import SunSideMiningStation.Location;

abstract class Robot {
    protected Location _location = Location.AT_BASE;
    protected Staff _passenger = null;

    public boolean carryHuman(Staff staff) {
        Robot carriedBy = staff.getCarriedBy();

        if (_passenger != null) {
            return false;
        }

        if (staff.getLocation() == _location && (carriedBy == null || carriedBy == this)) {
            staff.setCarriedBy(this);
            _passenger = staff;
            return true;
        }

        return false;
    }

    public boolean releaseHuman(Staff staff) {
        if (staff == _passenger && _passenger.getCarriedBy() == this) {
            _passenger.setCarriedBy(null);
            _passenger = null;

            return true;
        }

        return false;
    }

    public void move(Location location) {
        _location = location;

        if (_passenger != null)
            _passenger.setLocation(location);
    }

}
