import { Moment } from 'moment';
import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';

export interface IWorkshopDate {
  id?: number;
  date?: Moment;
  subscriptions?: IWorkshopSubscription[];
  workshopName?: string;
  workshopId?: number;
}

export class WorkshopDate implements IWorkshopDate {
  constructor(
    public id?: number,
    public date?: Moment,
    public subscriptions?: IWorkshopSubscription[],
    public workshopName?: string,
    public workshopId?: number
  ) {}
}
