import { Moment } from 'moment';
import { IUser } from 'app/core';

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
    user?: IUser;
}

export class WorkshopSubscription implements IWorkshopSubscription {
    constructor(
        public id?: number,
        public created?: Moment,
        public state?: SubscriptionState,
        public workshopDate?: string,
        public workshopId?: number,
        public user?: IUser
    ) {}
}
