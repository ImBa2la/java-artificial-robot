function out = pow_h(lf, lk, v, u )
    p = log(lk)/log(1-lk);
    t = log(lf)/log(1-lf);    
    if ndims(u) == 1
        y = 0;
        for i = 1:length(v)
            y = y + v(i)*(u(i)^(1/t));
        end        
    elseif  ndims(u) == 2
        [n,nn] = size(u);
        y = zeros(n);
        for i = 1:length(v)
            y(:) = y(:) + v(i).*(u(:,i).^(1/t));
        end
        y = y  .^ p;
    elseif ndims(u) == 3
        [n,nn, nnn] = size(u);
        y = zeros(n,nn);        
        for i = 1:length(v)
            y(:,:) = y(:,:) + v(i).*(u(:,:,i).^(1/t));
        end
        y = y .^ p;        
    end
    out = y;
end