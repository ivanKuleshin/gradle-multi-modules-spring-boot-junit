package utils.builders;

import org.example.model.Address;

public class EmployeeAddressBuilder {

        public static Address buildDefaultAddress() {
            return Address.builder()
                    .country("USA")
                    .city("New York")
                    .zip("12345")
                    .build();
        }
}
