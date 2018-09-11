import { Moment } from 'moment';

export const enum SubscriptionState {
    NEW = 'NEW',
    PAYED = 'PAYED',
    CANCELLED = 'CANCELLED'
}

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
