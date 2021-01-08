package com.egzosn.pay.paypal.bean.order;


public class FmfDetails{
	/**
	 * Type of filter.
	 */
	private String filterType;
	/**
	 * Filter Identifier.
	 */
	private String filterId;
	/**
	 * Name of the filter
	 */
	private String name;
	/**
	 * Description of the filter.
	 */
	private String description;


	public String getFilterType() {
		return this.filterType;
	}


	public String getFilterId() {
		return this.filterId;
	}

	public String getName() {
		return this.name;
	}


	
	public String getDescription() {
		return this.description;
	}


	public FmfDetails setFilterType(final String filterType) {
		this.filterType = filterType;
		return this;
	}


	public FmfDetails setFilterId(final String filterId) {
		this.filterId = filterId;
		return this;
	}


	
	public FmfDetails setName(final String name) {
		this.name = name;
		return this;
	}


	
	public FmfDetails setDescription(final String description) {
		this.description = description;
		return this;
	}

	@Override
	
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof FmfDetails)) return false;
		final FmfDetails other = (FmfDetails) o;
		if (!other.canEqual((Object) this)) return false;
		if (!super.equals(o)) return false;
		final Object this$filterType = this.getFilterType();
		final Object other$filterType = other.getFilterType();
		if (this$filterType == null ? other$filterType != null : !this$filterType.equals(other$filterType)) return false;
		final Object this$filterId = this.getFilterId();
		final Object other$filterId = other.getFilterId();
		if (this$filterId == null ? other$filterId != null : !this$filterId.equals(other$filterId)) return false;
		final Object this$name = this.getName();
		final Object other$name = other.getName();
		if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
		final Object this$description = this.getDescription();
		final Object other$description = other.getDescription();
		if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
		return true;
	}

	
	protected boolean canEqual(final Object other) {
		return other instanceof FmfDetails;
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + super.hashCode();
		final Object $filterType = this.getFilterType();
		result = result * PRIME + ($filterType == null ? 43 : $filterType.hashCode());
		final Object $filterId = this.getFilterId();
		result = result * PRIME + ($filterId == null ? 43 : $filterId.hashCode());
		final Object $name = this.getName();
		result = result * PRIME + ($name == null ? 43 : $name.hashCode());
		final Object $description = this.getDescription();
		result = result * PRIME + ($description == null ? 43 : $description.hashCode());
		return result;
	}
}
