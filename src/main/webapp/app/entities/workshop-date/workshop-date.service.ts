import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IWorkshopDate } from 'app/shared/model/workshop-date.model';

type EntityResponseType = HttpResponse<IWorkshopDate>;
type EntityArrayResponseType = HttpResponse<IWorkshopDate[]>;

@Injectable({ providedIn: 'root' })
export class WorkshopDateService {
  public resourceUrl = SERVER_API_URL + 'api/workshop-dates';

  constructor(protected http: HttpClient) {}

  create(workshopDate: IWorkshopDate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workshopDate);
    return this.http
      .post<IWorkshopDate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(workshopDate: IWorkshopDate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workshopDate);
    return this.http
      .put<IWorkshopDate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorkshopDate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkshopDate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(workshopDate: IWorkshopDate): IWorkshopDate {
    const copy: IWorkshopDate = Object.assign({}, workshopDate, {
      date: workshopDate.date != null && workshopDate.date.isValid() ? workshopDate.date.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workshopDate: IWorkshopDate) => {
        workshopDate.date = workshopDate.date != null ? moment(workshopDate.date) : null;
      });
    }
    return res;
  }
}
