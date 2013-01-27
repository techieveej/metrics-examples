package lab.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lab.api.CustomerResource;
import lab.domain.Customer;
import lab.http.HttpFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

@Component
public class CustomerService {

    private static final Log LOG = LogFactory.getLog(CustomerResource.class);

    private final Map<String, Customer> customerMap = Collections.synchronizedMap(new HashMap<String, Customer>());

    public Customer newCustomer(final Customer customer) {

        if (customerMap.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Customer already exists for id: " + customer.getId());
        }

        if (customer.getAddress() == null) {
            final String address = findAddressInformation();
            customer.setAddress(address);
        }

        customerMap.put(customer.getId(), customer);
        LOG.debug("Customer map: " + customerMap);

        return customer;
    }

    public Customer findCustomer(final String customerId) {

        if (customerMap.containsKey(customerId)) {
            return customerMap.get(customerId);
        }

        return null;
    }

    private String findAddressInformation() {

        final HttpClient http = HttpFactory.getHttp();

        final HttpGet httpget = new HttpGet("http://localhost:8080/http-service-stub/myresource/address");

        HttpResponse resp;
        try {
            resp = http.execute(httpget);
        } catch (final Exception e) {
            throw new RuntimeException("Unable to call address service", e);
        } finally {
            httpget.releaseConnection();
        }

        if (resp != null && resp.getStatusLine().getStatusCode() == 200) {
            try {
                return IOUtils.toString(resp.getEntity().getContent());
            } catch (final Exception e) {
                throw new RuntimeException("Unable to extract customer address from response", e);
            }
        } else {
            throw new RuntimeException("Invalid response from address service: " + resp.getStatusLine().getStatusCode());
        }
    }
}