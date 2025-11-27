import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BeneficiosComponent } from './beneficios/beneficios.component';
import { TransferenciaComponent } from './transferencia/transferencia.component';

const routes: Routes = [
  { path: '', redirectTo: 'beneficios', pathMatch: 'full' },
  { path: 'beneficios', component: BeneficiosComponent },
  { path: 'transferir', component: TransferenciaComponent },
  { path: '**', redirectTo: 'beneficios' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
