function out = plin_op(lf, lk, v, u )
    lf = 0.5;
    % lk < 0.5    
    e1 = @eq1;    
    function y = eq1(x)
        y = log(1 + lk.*x)./(1-lk) - log(1 + x);
    end
    
    C = fzero(e1,[0.001, 100]);
    b = 1;
    
    a = log(1 + lk*C) / C;
    if ndims(u) < 3 then
        y = sum(v*u');        
        y = (exp(C.*a.*y./b)-1)./C;
    else
        n = size(u);
        y = zeros(n(1:length(n)-1));
        for i = 1:length(v)
            y(:,:) = y(:,:) + v(i)*u(:,:,i);
        end                
        y = (exp(C.*a.*y./b)-1)./C;        
    end    
    out = y;
end