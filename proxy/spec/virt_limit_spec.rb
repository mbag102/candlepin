require 'candlepin_scenarios'

describe 'Virt Limit Products' do
  include CandlepinMethods
  it_should_behave_like 'Candlepin Scenarios'

  before(:each) do
    @owner = create_owner random_string('virt_owner')
    @user = user_client(@owner, random_string('virt_user'))

    @product = create_product(nil, nil, {
      :attributes => {
        :virt_limit => 3
      }
    })

    @sub = @cp.create_subscription(@owner.key, @product.id, 10)
    @cp.refresh_pools(@owner.key)

    @pools = @user.list_pools :owner => @owner.id, :product => @product.id
  end

  it 'should create 2 pools' do
    @pools.should have(2).things
  end

  it 'should create a physical pool with the given quantity' do
    # Just checking for one pool with no attributes
    @pools.find_all { |pool| pool.attributes.empty? }.should have(1).things
  end

  it 'should create a virtual pool' do
    @pools.find_all do
      |pool| is_virt? pool
    end.should have(1).things
  end

  it 'should create a virtual pool with quantity * virt_limit entitlements' do
    virt_pool = @pools.find { |pool| is_virt? pool }
    virt_pool.quantity.should == 30
  end

  it 'should update virtual pool quantity' do
    # Update subscription quantity:
    @sub.quantity = 20
    @cp.update_subscription(@sub)
    @cp.refresh_pools(@owner.key)
    @pools = @user.list_pools :owner => @owner.id, :product => @product.id
    virt_pool = @pools.find { |pool| is_virt? pool }
    virt_pool.quantity.should == 60
  end

  private

  def is_virt?(pool)
    pool.attributes.any? do |attr|
      attr.name == 'virt_only' && attr.value == 'true'
    end
  end

end