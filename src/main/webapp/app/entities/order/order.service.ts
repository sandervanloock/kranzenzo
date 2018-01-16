import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Order } from './order.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OrderService {

    private resourceUrl =  SERVER_API_URL + 'api/orders';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/orders';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(order: Order): Observable<Order> {
        const copy = this.convert(order);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(order: Order): Observable<Order> {
        const copy = this.convert(order);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Order> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Order.
     */
    private convertItemFromServer(json: any): Order {
        const entity: Order = Object.assign(new Order(), json);
        entity.created = this.dateUtils
            .convertDateTimeFromServer(json.created);
        entity.updated = this.dateUtils
            .convertDateTimeFromServer(json.updated);
        return entity;
    }

    /**
     * Convert a Order to a JSON which can be sent to the server.
     */
    private convert(order: Order): Order {
        const copy: Order = Object.assign({}, order);

        copy.created = this.dateUtils.toDate(order.created);

        copy.updated = this.dateUtils.toDate(order.updated);
        return copy;
    }
}
