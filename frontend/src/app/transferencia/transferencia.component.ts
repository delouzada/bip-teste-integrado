import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BeneficioService } from '../services/beneficio.service';

@Component({
  selector: 'app-transferencia',
  templateUrl: './transferencia.component.html',
  styleUrls: ['./transferencia.component.scss']
})
export class TransferenciaComponent {
  form: FormGroup;
  mensagem?: string;
  mensagemTipo: 'success' | 'error' | undefined;
  carregando = false;

  constructor(private fb: FormBuilder, private service: BeneficioService) {
    this.form = this.fb.group({
      fromId: [null, Validators.required],
      toId: [null, Validators.required],
      valor: [null, [Validators.required, Validators.min(1)]]
    });
  }

  transferir(): void {
    if (this.form.invalid) {
      this.mensagem = 'Preencha todos os campos corretamente.';
      this.mensagemTipo = 'error';
      return;
    }

    this.carregando = true;
    this.mensagem = undefined;
    this.mensagemTipo = undefined;

    this.service.transferir(this.form.value).subscribe({
      next: () => {
        this.mensagem = 'Transferencia realizada com sucesso.';
        this.mensagemTipo = 'success';
        this.form.reset();
      },
      error: (err) => {
        this.mensagem = err?.error?.message || 'Erro ao realizar transferencia.';
        this.mensagemTipo = 'error';
      }
    }).add(() => {
      this.carregando = false;
    });
  }
}
