package com.walletAPI.json;

public interface JsonViews {

    public interface Transaction {

        public interface Patch {
        }

        public interface Get extends Patch {
        }

    }

    public interface User {

        public interface Patch {
        }


    }

    public interface Account {

        public interface Patch {
        }
    }
}
