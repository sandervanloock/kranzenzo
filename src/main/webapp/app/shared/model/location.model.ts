export interface ILocation {
    id?: number;
    latitude?: number;
    longitude?: number;
    description?: string;
}

export class Location implements ILocation {
    constructor(public id?: number, public latitude?: number, public longitude?: number, public description?: string) {}
}
