import {BaseEntity, User} from './../../shared';

export const enum SubscriptionState {
    'NEW', 'PAYED', 'CANCELLED'
}

export class WorkshopSubscription implements BaseEntity {
    constructor( public id?: number, public created?: any, public state?: SubscriptionState, public workshopId?: number, public user?: User ) {
    }
}
