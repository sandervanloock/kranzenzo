import { Moment } from 'moment';
import { SubscriptionState } from 'app/shared/model/enumerations/subscription-state.model';

export interface IWorkshopSubscription {
  id?: number;
  created?: Moment;
  state?: SubscriptionState;
  workshopDate?: string;
  workshopId?: number;
}

export class WorkshopSubscription implements IWorkshopSubscription {
  constructor(
    public id?: number,
    public created?: Moment,
    public state?: SubscriptionState,
    public workshopDate?: string,
    public workshopId?: number
  ) {}
}
