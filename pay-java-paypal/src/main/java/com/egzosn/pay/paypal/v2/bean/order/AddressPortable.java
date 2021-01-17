package com.egzosn.pay.paypal.v2.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * The portable international postal address. Maps to [AddressValidationMetadata](https://github.com/googlei18n/libaddressinput/wiki/AddressValidationMetadata) and HTML 5.1 [Autofilling form controls: the autocomplete attribute](https://www.w3.org/TR/html51/sec-forms.html#autofilling-form-controls-the-autocomplete-attribute).
 */
public class AddressPortable {

    public AddressPortable() {
    }

    /**
     * The first line of the address. For example, number or street. For example, `173 Drury Lane`. Required for data entry and compliance and risk checks. Must contain the full address.
     */
    @JSONField(name = "address_line_1")
    private String addressLine1;

    public String addressLine1() {
        return addressLine1;
    }

    public AddressPortable addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    /**
     * The second line of the address. For example, suite or apartment number.
     */
    @JSONField(name = "address_line_2")
    private String addressLine2;

    public String addressLine2() {
        return addressLine2;
    }

    public AddressPortable addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    /**
     * The third line of the address, if needed. For example, a street complement for Brazil, direction text, such as `next to Walmart`, or a landmark in an Indian address.
     */
    @JSONField(name = "address_line_3")
    private String addressLine3;

    public String addressLine3() {
        return addressLine3;
    }

    public AddressPortable addressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
        return this;
    }

    /**
     * The highest level sub-division in a country, which is usually a province, state, or ISO-3166-2 subdivision. Format for postal delivery. For example, `CA` and not `California`. Value, by country, is:<ul><li>UK. A county.</li><li>US. A state.</li><li>Canada. A province.</li><li>Japan. A prefecture.</li><li>Switzerland. A kanton.</li></ul>
     */
    @JSONField(name = "admin_area_1")
    private String adminArea1;

    public String adminArea1() {
        return adminArea1;
    }

    public AddressPortable adminArea1(String adminArea1) {
        this.adminArea1 = adminArea1;
        return this;
    }

    /**
     * A city, town, or village. Smaller than `admin_area_level_1`.
     */
    @JSONField(name = "admin_area_2")
    private String adminArea2;

    public String adminArea2() {
        return adminArea2;
    }

    public AddressPortable adminArea2(String adminArea2) {
        this.adminArea2 = adminArea2;
        return this;
    }

    /**
     * A sub-locality, suburb, neighborhood, or district. Smaller than `admin_area_level_2`. Value is:<ul><li>Brazil. Suburb, bairro, or neighborhood.</li><li>India. Sub-locality or district. Street name information is not always available but a sub-locality or district can be a very small area.</li></ul>
     */
    @JSONField(name = "admin_area_3")
    private String adminArea3;

    public String adminArea3() {
        return adminArea3;
    }

    public AddressPortable adminArea3(String adminArea3) {
        this.adminArea3 = adminArea3;
        return this;
    }

    /**
     * The neighborhood, ward, or district. Smaller than `admin_area_level_3` or `sub_locality`. Value is:<ul><li>The postal sorting code for Guernsey and many French territories, such as French Guiana.</li><li>The fine-grained administrative levels in China.</li></ul>
     */
    @JSONField(name = "admin_area_4")
    private String adminArea4;

    public String adminArea4() {
        return adminArea4;
    }

    public AddressPortable adminArea4(String adminArea4) {
        this.adminArea4 = adminArea4;
        return this;
    }

    /**
     * REQUIRED
     * The [two-character ISO 3166-1 code](/docs/integration/direct/rest/country-codes/) that identifies the country or region.<blockquote><strong>Note:</strong> The country code for Great Britain is <code>GB</code> and not <code>UK</code> as used in the top-level domain names for that country. Use the `C2` country code for China worldwide for comparable uncontrolled price (CUP) method, bank card, and cross-border transactions.</blockquote>
     */
    @JSONField(name = "country_code")
    private String countryCode;

    public String countryCode() {
        return countryCode;
    }

    public AddressPortable countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    /**
     * The postal code, which is the zip code or equivalent. Typically required for countries with a postal code or an equivalent. See [postal code](https://en.wikipedia.org/wiki/Postal_code).
     */
    @JSONField(name = "postal_code")
    private String postalCode;

    public String postalCode() {
        return postalCode;
    }

    public AddressPortable postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAdminArea1() {
        return adminArea1;
    }

    public void setAdminArea1(String adminArea1) {
        this.adminArea1 = adminArea1;
    }

    public String getAdminArea2() {
        return adminArea2;
    }

    public void setAdminArea2(String adminArea2) {
        this.adminArea2 = adminArea2;
    }

    public String getAdminArea3() {
        return adminArea3;
    }

    public void setAdminArea3(String adminArea3) {
        this.adminArea3 = adminArea3;
    }

    public String getAdminArea4() {
        return adminArea4;
    }

    public void setAdminArea4(String adminArea4) {
        this.adminArea4 = adminArea4;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
