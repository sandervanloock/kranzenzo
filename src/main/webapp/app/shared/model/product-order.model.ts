import { Moment } from 'moment';
import { OrderState } from 'app/shared/model/enumerations/order-state.model';
import { DeliveryType } from 'app/shared/model/enumerations/delivery-type.model';
import { PaymentType } from 'app/shared/model/enumerations/payment-type.model';

export interface IProductOrder {
  id?: number;
  created?: Moment;
  updated?: Moment;
  state?: OrderState;
  deliveryType?: DeliveryType;
  includeBatteries?: boolean;
  description?: string;
  deliveryPrice?: number;
  paymentType?: PaymentType;
  customerId?: number;
  deliveryAddressId?: number;
  productName?: string;
  productId?: number;
}

export class ProductOrder implements IProductOrder {
  constructor(
    public id?: number,
    public created?: Moment,
    public updated?: Moment,
    public state?: OrderState,
    public deliveryType?: DeliveryType,
    public includeBatteries?: boolean,
    public description?: string,
    public deliveryPrice?: number,
    public paymentType?: PaymentType,
    public customerId?: number,
    public deliveryAddressId?: number,
    public productName?: string,
    public productId?: number
  ) {
    this.includeBatteries = this.includeBatteries || false;
  }
}
