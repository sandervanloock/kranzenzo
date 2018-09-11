import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';

type EntityResponseType = HttpResponse<IWorkshopSubscription>;
type EntityArrayResponseType = HttpResponse<IWorkshopSubscription[]>;

@Injectable({ providedIn: 'root' })
export class WorkshopSubscriptionService {
    private resourceUrl = SERVER_API_URL + 'api/workshop-subscriptions';

    constructor(private http: HttpClient) {}

    create(workshopSubscription: IWorkshopSubscription): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(workshopSubscription);
        return this.http
            .post<IWorkshopSubscription>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(workshopSubscription: IWorkshopSubscription): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(workshopSubscription);
        return this.http
            .put<IWorkshopSubscription>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IWorkshopSubscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IWorkshopSubscription[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(workshopSubscription: IWorkshopSubscription): IWorkshopSubscription {
        const copy: IWorkshopSubscription = Object.assign({}, workshopSubscription, {
            created:
                workshopSubscription.created != null && workshopSubscription.created.isValid()
                    ? workshopSubscription.created.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((workshopSubscription: IWorkshopSubscription) => {
            workshopSubscription.created = workshopSubscription.created != null ? moment(workshopSubscription.created) : null;
        });
        return res;
    }
}
